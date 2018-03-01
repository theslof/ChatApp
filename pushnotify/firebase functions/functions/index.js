const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();
// Get the Messaging service for the default app
var defaultMessaging = admin.messaging();

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
    // Send Push Message

    // Send a message
    var message = {
      notification: {
        title: 'New message',
        body: 'There are new messages available'
      },
      topic: cid};

   return defaultMessaging.send(message)
      .then((response) => {
        // Response is a message ID string.
        console.log('Successfully sent message:', response);
      })
      .catch((error) => {
        console.log('Error sending message:', error);
      });
});

//https://firebase.google.com/docs/functions/get-started?authuser=0
//Deploy from console with: firebase deploy --only functions