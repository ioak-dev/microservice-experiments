const { exchangeCodeForToken, verifyIdToken } = require('../services/googleAuthService');
const axios = require('axios');

const authenticateUser = async (req, res) => {
    try {
        const { code } = req.body;
        const { accessToken, idToken } = await exchangeCodeForToken(code);
        const payload = await verifyIdToken(idToken);

        const userDetails = await axios.get('https://www.googleapis.com/oauth2/v3/userinfo', {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        });

        const userManagementResponse = await axios.post('http://user-service:5000/api/users', userDetails.data);

        if (userManagementResponse.status === 200) {
            res.status(200).json({ message: 'Authentication successful', userDetails: userDetails.data });
        } else {
            throw new Error('User management service error');
        }
    } catch (error) {
        console.error('Error during authentication:', error);
        res.status(500).json({ message: 'Authentication failed', error: error.message });
    }
};

module.exports = { authenticateUser };
