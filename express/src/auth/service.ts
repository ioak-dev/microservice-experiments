const axios = require('axios');

const authBaseUrl = "https://api.ioak.io:8010/api";
const realmId = "228";

const generateTokenFromAuthlite = async (email: any, password: any) => {
    try {
        const authliteResponse = await axios.post(
            `${authBaseUrl}/${realmId}/user/auth/signin`,
            {
                response_type: 'token',
                email,
                password,
            }
        );
        if (authliteResponse.status === 200) {
            const { access_token, refresh_token } = authliteResponse.data;
            console.log('Access token: ',access_token);
            return {
                status: 200,
                data: {
                    access_token,
                    refresh_token
                }
            };
        } else {
            return {
                status: authliteResponse.status,
                data: { error: 'Signin failed' }
            };
        }
    } catch (error) {
        console.error('Error during signin:', error);
        throw new Error('Internal server error');
    }
};

module.exports = { generateTokenFromAuthlite };
