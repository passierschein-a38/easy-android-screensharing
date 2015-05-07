var WebSocketServer = require('websocket').server;
var http = require('http');

var configFrame = null;

var start = new Object();
start.id = 100; //shower start
start = JSON.stringify( start );

var close = new Object();
close.id = 200; //shower start
close = JSON.stringify( close );

var msg = new Object();
msg.id = 300; //shower start
msg.text = "";

var pin = null;

//sender server
var server = http.createServer(function(request, response) {    
});

server.listen(5000, function() { });

// create the server
wsServer = new WebSocketServer({
    httpServer: server,
	maxReceivedFrameSize: 64*1024*1024,   // 64MiB
	maxReceivedMessageSize: 64*1024*1024, // 64MiB
	fragmentOutgoingMessages: true
});

// WebSocket server
wsServer.on('request', function(request) {
    
	var connection = request.accept(null, request.origin);
	connection.auth=false;
		
    connection.on('message', function(message) {
		console.log( 'incomming watcher message:' + JSON.stringify( message ) );             	              
		
		if( connection.auth == false ){
			
			if( message.type != 'utf8' ){
				return;
			}
			    
			if( message.utf8Data == pin ){
				console.log( 'auth watcher successfully:' );             	              
				connection.auth = true;
				
				//inform shower
				
				wsShowerServer.connections.forEach(function (conn) {								
					conn.send( "watcher" );						
				});			
				
				if( configFrame != null ){
					connection.send( start );
					connection.sendBytes( configFrame );		
				}
			}else{
				msg.text = "Unknown pin code";
				connection.send( JSON.stringify( msg ) );
			}
		}			
    });

    connection.on('close', function(connection) {      
    });
});


var showerServer = http.createServer(function(request, response) {    
});

showerServer.listen(5001, function() { });

// create the server
wsShowerServer = new WebSocketServer({
    httpServer: showerServer,
	maxReceivedFrameSize: 64*1024*1024,   // 64MiB
	maxReceivedMessageSize: 64*1024*1024, // 64MiB
	fragmentOutgoingMessages: true
});

wsShowerServer.on('request', function(request) {

    var connection = request.accept(null, request.origin);	
	
	connection.on('message', function(message) {
			
		if( null == pin ){
			if( message.type == 'utf8' ){
			    pin = message.utf8Data;		
			    console.log( 'received shower pin:' + pin); 				
				return;
			}						
		}		
		
		if( configFrame == null ){
		    console.log( 'start shower connection:');             
			configFrame = message.binaryData;
			
			//inform clients - we are starting now
			wsServer.connections.forEach(function (conn) {		
				if( conn.auth == true ){
					conn.send( start );
				}
			})	
		}
	
		wsServer.connections.forEach(function (conn) {			
			if( conn.auth == true ){		
				conn.sendBytes( message.binaryData );						
			}
		})			
    });
	
    connection.on('close', function(connection) {
        console.log( 'close shower connection:');             
		configFrame=null;			
		pin=null;
		
		//inform clients - we are stopping now
		wsServer.connections.forEach(function (conn) {				
				conn.auth=false;
				conn.send( close );
		})	
				
		});		
    });
