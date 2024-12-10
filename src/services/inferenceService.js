const tf = require('@tensorflow/tfjs-node');

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
        const confidenceScore = Math.max(...predictionData) * 100; //  Score multiplied by 100 to get percentage

        return { confidenceScore, classIndex};

    } catch (error) {
        console.error('Error during prediction:', error);
        throw new Error(`Failed to predict the batik class: ${error.message}`);
    }
}

module.exports = predictBatikClass;
