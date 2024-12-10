const { Firestore } = require('@google-cloud/firestore');
const { Storage } = require('@google-cloud/storage');
const serviceAccount = require("../../adminsdk.json"); //your cab also use firebase service account token here

//firestore databases
const dbMetadata = new Firestore({
    databaseId: "metadata",
});
const dbHistory = new Firestore({
    databaseId: "history",
});

//cloud bucket storage (batik image)
const imgStorage = new Storage({
    projectId: serviceAccount.project_id,
    credentials: serviceAccount,
});

module.exports = {
    dbMetadata,
    dbHistory,
    imgStorage
};
  