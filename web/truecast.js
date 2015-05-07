function pushNotification( text )
{
  /*// Let's check if the browser supports notifications
  if (!("Notification" in window)) {
    alert("This browser does not support desktop notification");
  }

  // Let's check if the user is okay to get some notification
  else if (Notification.permission === "granted") {
    // If it's okay let's create a notification
    var notification = new Notification(text);
  }

  // Otherwise, we need to ask the user for permission
  else if (Notification.permission !== 'denied') {
    Notification.requestPermission(function (permission) {
      // If the user is okay, let's create a notification
      if (permission === "granted") {
        var notification = new Notification(text);
      }
    });
  }

  // At last, if the user already denied any notification, and you 
  // want to be respectful there is no need to bother them any more.*/
}

function initPlayer(){
		var p = new Player({
			useWorker: true,
			workerFile: 'Broadway/Player/Decoder.js',  
		});
		
		document.player = p;	
		
		$('#player').append( document.player.canvas );
}

function initSockets(){

	var connection = new WebSocket('ws://localhost:5000');
	document.connection = connection;
	connection.binaryType = 'arraybuffer';

	connection.onopen = function () {
	};

	connection.onerror = function (error) {  
	};

	connection.onclose = function (evt) {
		$('#playerView').hide();				
		$('#pinView').show();		
		pushNotification( 'Connection lost to server' );
	};

	connection.onmessage = function (e) { 

	if( typeof e.data == 'object' ){
		var bytearray = new Uint8Array(e.data);
		document.player.decode( bytearray );  
		return;
	}else{
		var cmd = JSON.parse( e.data );

		console.log( cmd );
			
		switch( cmd.id ){
			case 100:	//shower start
				$('#player').append( document.player.canvas );
				$('#pinView').hide();
				$('#playerView').show();
				break;
			case 200:	//shower stop
				$('#playerView').hide();				
				$('#pinView').show();				
				pushNotification( 'Sharing stopped by peer' );
				break;	
			case 300:				
				pushNotification( cmd.text );			
				break;
		}		
	}
};
}

function connect(){
	var pin = $('#pinCode').val();
	document.connection.send( pin );	
    return false;	
}