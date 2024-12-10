const admin = require("firebase-admin");
const Boom = require("@hapi/boom"); // Tambahkan ini

async function authenticateUser(request, h) {
    const token = request.headers.authorization?.split(' ')[1]; // Format: "Bearer <token>"
    if (!token) {
        throw Boom.unauthorized('Token not provided');
    }

    try {
        const decodedToken = await admin.auth().verifyIdToken(token);
        request.auth = { uid: decodedToken.uid };
        return h.continue;
    } catch (error) {
        throw Boom.unauthorized('Invalid or expired token');
    }
}

module.exports = authenticateUser;
