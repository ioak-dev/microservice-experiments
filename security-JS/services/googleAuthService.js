const { OAuth2Client } = require('google-auth-library');
const axios = require('axios');
const qs = require('qs');

const oauth2Client = new OAuth2Client(process.env.GOOGLE_CLIENT_ID, process.env.GOOGLE_CLIENT_SECRET, process.env.REDIRECT_URI);

async function exchangeCodeForToken( code) {
    const tokenURL = 'https://oauth2.googleapis.com/token';
    try {
        console.log('Exchanging code for token with client_id:', process.env.GOOGLE_CLIENT_ID);
        console.log('Authorization code:', code);
        
        const response = await axios.post(tokenURL, qs.stringify({
            code: code,
            client_id: "1080184469568-apl20tffhv0fthvevi6uh44bqlj7ambk.apps.googleusercontent.com", 
            client_secret: "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",
            redirect_uri: "http://localhost:6006/",
            grant_type: 'authorization_code'
           
        }), {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        });
        console.log('Token exchange response:', response.data);
        return {
            accessToken: response.data.access_token,
            idToken: response.data.id_token
        };
    } catch (error) {
        console.error('Error exchanging authorization code:', error.response ? error.response.data : error.message);
        throw new Error('Failed to exchange authorization code');
    }
  }

const verifyIdTokenforGoogle = async (id_token) => {
    const ticket = await oauth2Client.verifyIdToken({
        idToken: id_token,
        audience: process.env.GOOGLE_CLIENT_ID
    });
    return ticket.getPayload();
};

module.exports = { exchangeCodeForToken, verifyIdTokenforGoogle };
