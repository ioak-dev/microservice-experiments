const transporter = require('./config');
const { totp } = require('otplib');
totp.options = { step: 300 };

const generateOtp = (secret:any) => {
  return totp.generate(secret);
};

const sendOtp = async (email:any, secret:any) => {
  const otp = generateOtp(secret);

  const mailOptions = {
    from: process.env.EMAIL,
    to: email,
    subject: 'Your OTP Code',
    text: `Your OTP is: ${otp}`
  };

  try {
    const info = await transporter.sendMail(mailOptions);
    console.log('Email sent:', info.response);
  } catch (err) {
    console.error('Error sending email:', err);
  }
};

module.exports = sendOtp;
