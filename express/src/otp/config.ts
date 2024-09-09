const nodemailer = require('nodemailer');

export const transporter = nodemailer.createTransport({
  service: 'gmail', 
  auth: {
    user: "testuser123@gmail.com", 
    pass: "abcc ijzk pjqy pkml" 
  }
});

module.exports = transporter;
