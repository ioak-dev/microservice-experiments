import dotenv from 'dotenv';
import express, { Request, Response } from 'express';
import {generateToken} from './src/jwtutil/jwt';
import authenticateToken from './src/jwtutil/middleware';
import { totp } from 'otplib';
const { generateTokenFromAuthlite } = require('./src/auth/service');
const bodyParser = require('body-parser');
const sendOtp = require('./src/otp/service');
const { authenticator } = require('otplib');
const otplib = require('otplib');
const qrcode = require('qrcode');
const twilio = require('twilio');

const app = express();
const port = 3000;
dotenv.config();
app.use(express.json());

const secret = authenticator.generateSecret();
const user = { id: 1, username: 'testuser' };

const otp = totp.generate(secret);

app.get('/get', (req: Request, res: Response) => {
    res.send("Hello express js");
});

app.post('/signin', async (req, res) => {
    const { email, password } = req.body;
    try {
      const authliteResponse = await generateTokenFromAuthlite(email, password);
      res.status(authliteResponse.status).json(authliteResponse.data);
    } catch (error) {
      console.error('Error during signin:', error);
      res.status(500).json({ error: 'Internal server error' });
    }
  });

app.post('/token', (req: Request, res: Response) => {
    const token = generateToken(user);
    res.json({ token });
});

const verifyOtp = (otp:any, secret:any) => {
  return totp.check(otp, secret);
};

app.post('/send-otp-mail', async (req, res) => {
  const { email, secret } = req.body;

  if (!email || !secret) {
    return res.status(400).send('Email and secret are required');
  }

  try {
    await sendOtp(email, secret);
    res.status(200).send('OTP sent successfully');
  } catch (err) {
    res.status(500).send('Error sending OTP');
  }
});

app.post('/verify-otp', (req, res) => {
  const { otp, secret } = req.body;

  if (!otp || !secret) {
    return res.status(400).send('OTP and secret are required');
  }

  const isValid = verifyOtp(otp, secret);

  if (isValid) {
    res.status(200).send('OTP is valid');
  } else {
    res.status(400).send('OTP is invalid or expired');
  }
});


//

interface UserSecrets {
  [username: string]: string;
}
app.use(bodyParser.json());
const userSecrets: UserSecrets = {};
app.post('/generate', (req, res) => {
  const { username } = req.body;
  const secret = otplib.authenticator.generateSecret();
  userSecrets[username] = secret;
  const otpauth = otplib.authenticator.keyuri(username, 'ExpressApp', secret);
  qrcode.toDataURL(otpauth, (err:any, imageUrl:any) => {
    if (err) {
      res.status(500).send('Error generating QR code');
      return;
    }
    res.send(`<img src="${imageUrl}">`);
  });
});

app.post('/verify', (req, res) => {
  const { username, token } = req.body;
  const secret = userSecrets[username];
  if (!secret) {
    res.status(400).send('User not found');
    return;
  }
  const isValid = otplib.authenticator.check(token, secret);
  if (isValid) {
    res.send('OTP is valid');
  } else {
    res.send('OTP is invalid');
  }
});

app.get("/generate/secret",(req,res)=>{
  const secret= authenticator.generateSecret();
  res.send(secret);
});


//SMS based otp generation

const accountSid = process.env.TWILIO_ACCOUNT_SID;
const authToken = process.env.TWILIO_AUTH_TOKEN;
const client = new twilio(accountSid, authToken);

type OtpStore = {
  [key: string]: number;
};

const otpStore: OtpStore = {};

app.post('/send-otp/sms', (req:any, res:any) => {
    const { phoneNumber } = req.body;
    const otp = Math.floor(100000 + Math.random() * 900000);
    otpStore[phoneNumber] = otp;

    client.messages.create({
        body: `Your OTP is ${otp}`,
        from: process.env.TWILIO_PHONE_NUMBER,
        to: phoneNumber
    }).then((message:any) => {
        res.status(200).send(`OTP sent to ${phoneNumber}`);
    }).catch((err:any) => {
        res.status(500).send(`Failed to send OTP: ${err.message}`);
    });
});


app.post('/verify-otp/sms', (req, res) => {
    const { phoneNumber, otp } = req.body;
    if (otpStore[phoneNumber] && otpStore[phoneNumber] === parseInt(otp)) {
        delete otpStore[phoneNumber]; 
        res.status(200).send('OTP verified successfully');
    } else {
        res.status(400).send('Invalid OTP');
    }
});


app.listen(port, () => {
  console.log(`App started at http://localhost:${port}`);
});