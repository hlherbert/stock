var http = require('http');

let server = http.createServer(function (request, response) {
    response.writeHead(200, {'Content-Type':'text/plain'});
    response.end('Hellow World\n');
}).listen(8080);

setTimeout( () => {
    server.close();
    console.log("Server is close");
}, 30000);

console.log('Server running at http://127.0.0.1:8080/');