const { postPredictBatikHandler, getMetadataHandler } = require('./handler');
const authenticateUser = require('../middlewares/authenticateUser');
 
const routes = [
  {
    path: '/predict',
    method: 'POST',
    handler: postPredictBatikHandler,
    options: {
      pre: [{ method: authenticateUser }], // Middleware untuk autentikasi
      payload: {
        allow: 'multipart/form-data',
        multipart: true
      }
    }
  },
  {
    path: "/batikmetadata/{id}",
    method: "GET",
    handler: getMetadataHandler,
  },
]
 
module.exports = routes;