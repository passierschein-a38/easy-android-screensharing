var WebSocketServer = require('websocket').server;
var http = require('http');

var configFrame = null;

var start = new Object();
start.id = 100; //shower start
start = JSON.stringify( start );

var close = new Object();
close.id = 200; //shower start
close = JSON.stringify( close );

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
	
	if( configFrame != null ){
		connection.send( start );
		connection.sendBytes( configFrame );		
	}
		
    connection.on('message', function(message) {
		console.log( 'incomming watcher message:' + JSON.stringify( message ) );             	              
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
			
		if( configFrame == null ){
		    console.log( 'start shower connection:');             
			configFrame = message.binaryData;
			
			//inform clients - we are starting now
			wsServer.connections.forEach(function (conn) {			   
				conn.send( start );
			})	
		}
	
		wsServer.connections.forEach(function (conn) {					
			conn.sendBytes( message.binaryData );						
		})			
    });
	
    connection.on('close', function(connection) {
        console.log( 'close shower connection:');             
		configFrame=null;			
		
		//inform clients - we are stopping now
		wsServer.connections.forEach(function (conn) {			   
			conn.send( close );
		})	
				
		});		
    });
