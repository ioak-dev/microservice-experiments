const express = require('express');
const { authenticateUser } = require('../controllers/authController');

const router = express.Router();

router.get('/auth', authenticateUser);

module.exports = router;
