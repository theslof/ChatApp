const functions = require('firebase-functions');

//Trigger a function when a new document is created in messages
exports.createmessage = functions.firestore
  .document('messages/{documentId}')
  .onCreate(event => {
    // Get an object representing the document
    // e.g. {'name': 'Marie', 'age': 66}
    var newValue = event.data.data();

    // access a particular field as you would any JS property
    var cid = newValue.cid;

    // perform desired operations ...
});

//https://firebase.google.com/docs/functions/get-started?authuser=0
//Deploy from console with: firebase deploy --only functions