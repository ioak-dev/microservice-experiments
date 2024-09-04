const { exchangeCodeForToken, verifyIdTokenforGoogle } = require('../services/googleAuthService');
const axios = require('axios');

const authenticateUser = async (req, res) => {
    try {

        const { code } = req.query;
        if (!code) {
            throw new Error('Invalid input: code is required');
        }
         const { accessToken, idToken } = await exchangeCodeForToken(code); 
        const payload = await verifyIdTokenforGoogle(idToken);

        console. log(payload)
        const userDetails = await axios.get('https://www.googleapis.com/oauth2/v3/userinfo', {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        });

        if (userDetails.status === 200) {
            res.status(200).json({ message: 'Authentication successful', userDetails: userDetails.data });
        } else {
            throw new Error('Internal Service error');
        }
    } catch (error) {
        console.error('Error during authentication:', error);
        res.status(500).json({ message: 'Authentication failed', error: error.message });
    }
};

module.exports = { authenticateUser };
