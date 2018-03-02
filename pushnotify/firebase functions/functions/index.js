const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();
// Get the Messaging service ( Firebase Cloud Messaging)for the default app
var defaultMessaging = admin.messaging();

//Trigger a function when a new document is created in messages
exports.newmessage = functions.firestore
  .document('messages/{documentId}')
  .onCreate(event => {
    // Get an object representing the document
    // e.g. {'name': 'Marie', 'age': 66}
    var newValue = event.data.data();

    // access channel id from message
    var cid = newValue.cid;
    var action = "NewMessages";

    // Send Push Message
    var message = {
    android: {
    ttl: 60 * 1000,
    notification: {
        title: cid,
        body: 'New message(s)',
        tag: '1',
        clickAction: action
        }
      },
      data:{
        cid : cid
      },
      topic: cid
      };

   return defaultMessaging.send(message)
      .then((response) => {
        // Response is a message ID string.
        console.log('Successfully sent message:', response);
        console.log('action:', action);
      })
      .catch((error) => {
        console.log('Error sending message:', error);
      });
});

//https://firebase.google.com/docs/functions/get-started?authuser=0
//Deploy from console with: firebase deploy --only functions