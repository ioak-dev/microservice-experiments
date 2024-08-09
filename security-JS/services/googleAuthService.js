const { OAuth2Client } = require('google-auth-library');
const axios = require('axios');

const oauth2Client = new OAuth2Client(process.env.GOOGLE_CLIENT_ID);

const exchangeCodeForToken = async (code) => {
    const response = await axios.post('https://oauth2.googleapis.com/token', {
        code,
        client_id: process.env.GOOGLE_CLIENT_ID,
        client_secret: process.env.GOOGLE_CLIENT_SECRET,
        redirect_uri: process.env.REDIRECT_URI,
        grant_type: 'authorization_code'
    });
    return { accessToken: response.data.access_token, idToken: response.data.id_token };
};

const verifyIdToken = async (idToken) => {
    const ticket = await oauth2Client.verifyIdToken({
        idToken,
        audience: process.env.GOOGLE_CLIENT_ID
    });
    return ticket.getPayload();
};

module.exports = { exchangeCodeForToken, verifyIdToken };
