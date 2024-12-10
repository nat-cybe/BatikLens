const tf = require('@tensorflow/tfjs-node');

// Mapping class names to indices
const className = {
    0: 'Batik Cendrawasih',
    1: 'Batik Dayak',
    2: 'Batik Geblek Renteng',
    3: 'Batik Megamendung',
    4: 'Batik Parang',
    5: 'Batik Pring Sedawung',
    6: 'Batik Tambal',
    7: 'Batik Truntum'
};

async function predictBatikClass(model, image) {
    try {
        //Aku gk ngerti ini apa jgn nanya 

        // Load the image buffer as a tensor
        const tensor = tf.node.decodeImage(image, 3) // Decode to RGB
            .resizeNearestNeighbor([256, 256]) // Resize to 256x256
            .expandDims(0) // Add batch dimension
            .toFloat(); // Convert to float32

        // Make a prediction
        const prediction = model.predict(tensor);

        // Get the predicted class index and confidence score
        const predictionData = prediction.dataSync(); // Get prediction as an array
        const classIndex = prediction.argMax(-1).dataSync()[0]; // Get the index of the highest score
        // const confidenceScore = predictionData[classIndex]; // Get confidence for the predicted class
        const confidenceScore = Math.max(...predictionData) * 100; //  Score multiplied by 100 to get percentage

        // Get the class name
        const idBatik = classIndex + 1;
        const label = className[classIndex];
        const namaBatik = label; // Assuming the label is equivalent to the "namaBatik"

        // disini akan diisi kode untuk menambahkan informasi mengenai metadata batik seperti asal, nama, filosofi unik dan informasi lainnya

        return { confidenceScore, label, namaBatik, idBatik};

    } catch (error) {
        console.error('Error during prediction:', error);
        throw new Error(`Failed to predict the batik class: ${error.message}`);
    }
}

module.exports = predictBatikClass;
