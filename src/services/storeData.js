const { Firestore } = require('@google-cloud/firestore');
 
async function storeData(id, data, uid) {
    try {
        const dbHistory = new Firestore({
            databaseId: 'history',
        });
        
        // Tambahkan ke Firestore

        const predictHistory = dbHistory.collection('usersHistories').doc(uid).collection('history');
        // await historyRef.add({
        //     namaBatik,
        //     confidenceScore,
        //     createdAt: new Date().toISOString()
        // }); //2

        // const predictCollection = dbHistory.collection('history'); //1
        return predictHistory.doc(id).set(data);

      

    } catch (error) {
        throw error;
    }
   
}
 
module.exports = storeData;