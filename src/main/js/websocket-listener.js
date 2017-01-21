'use strict';

let SockJS = require('sockjs-client');
const Stomp = require('stompjs');

function register(registrations) {
	let socket = SockJS('/chan');
	let stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		registrations.forEach(function (registration) {
			stompClient.subscribe(registration.route, registration.callback, registration.headers);
		});
	});
}

module.exports.register = register;
