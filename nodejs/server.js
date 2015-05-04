var WebSocketServer = require('websocket').server;
var http = require('http');

var configFrame = null;

//sender server
var server = http.createServer(function(request, response) {    
});

server.listen(5000, function() { });

// create the server
wsServer = new WebSocketServer({
    httpServer: server,
	maxReceivedFrameSize: 64*1024*1024,   // 64MiB
	maxReceivedMessageSize: 64*1024*1024, // 64MiB
});

// WebSocket server
wsServer.on('request', function(request) {
    
	var connection = request.accept(null, request.origin);
	
	if( configFrame != null ){
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
	maxReceivedFrameSize: 0x10000*2,
	maxReceivedMessageSize: 0x100000*2,
});

wsShowerServer.on('request', function(request) {

    var connection = request.accept(null, request.origin);	
	
	connection.on('message', function(message) {
			
		if( configFrame == null ){
			configFrame = message.binaryData;
		}
	
		wsServer.connections.forEach(function (conn) {					
			conn.sendBytes( message.binaryData );						
		})			
    });
	
    connection.on('close', function(connection) {
        console.log( 'close shower connection:');             
		configFrame=null;			
		});		
    });
