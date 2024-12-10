const predictBatikClass = require('../services/inferenceService');
const storeData = require('../services/storeData');
const { Firestore } = require('@google-cloud/firestore');
const crypto = require('crypto');

async function postPredictBatikHandler(request, h) {
  const { uid } = request.auth; // UID pengguna
  const { image } = request.payload;
  const { model } = request.server.app;

  try {
    const { confidenceScore, label, namaBatik } = await predictBatikClass(model, image);

    const id = crypto.randomUUID();
    const createdAt = new Date().toISOString();

    const data = {
      "id": id,
      "namaBatik": namaBatik,
      "result": label,
      "confidenceScore": confidenceScore,
      "createdAt": createdAt
    };

    await storeData(id, data, uid);

    const response = h.response({
      status: 'success',
      message: 'Model is predicted successfully. And saved to history',
      data
    })
    response.code(201);
    return response;
    
  } catch (error) {
    // This will be handled by the onPreResponse extension in server.js
    throw error;
  }
}

const getMetadataHandler = async (request, h) => {
  const { id } = request.params;
  const dbMetadata = new Firestore({
    databaseId: 'metadata',
  });

  try {
      const batikDoc = dbMetadata.collection("metadata").doc(id.toString());
      const snapshot = await batikDoc.get();

      if (!snapshot.exists) {
          return h.response({
              success: false,
              message: `Batik dengan ID "${id}" tidak ditemukan.`,
          }).code(404);
      }

      return h.response({ 
        success: true, 
        data: snapshot.data() 
      }).code(200);

  } catch (error) {
      console.error(error);
      return h.response({ 
        success: false, 
        message: error.message 
      }).code(500);
  }
};

module.exports = {postPredictBatikHandler, getMetadataHandler};