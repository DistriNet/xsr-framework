var CACHE_NAME = 'leaktest-v1';
var urlsToCache = [];

self.addEventListener('install', function(event) {
    event.waitUntil(
        caches.open(CACHE_NAME).then(cache =>
        { console.log('Opened cache');
    return cache.addAll(urlsToCache);
},
    reason => console.log('Opening cache did not succeed', reason))
);
    self.skipWaiting(); // This SW will become the active one
});

self.addEventListener('activate', function(event) {
    console.log('SW activated');
    return self.clients.claim();
});

self.addEventListener('message', function(event) {
    var data = event.data;

    if (data.command == "twoWayCommunication") {
        console.log("Responding message from page: ", data.message);
        event.ports[0].postMessage({
            "message": "Active"
        });
    }
    // Testing potential leaks
    // - Import scripts
    try {
        importScripts('https://leak.test/import-scripts')
    } catch(e) { console.log('import-scripts: ', e); }
    // - Fetch:
    try {
        var myRequest = new Request('https://leak.test/fetch');
        fetch(myRequest);
    } catch(e) { console.log('fetch: ', e); }
    // - Fetch: credentials-include
    try {
        var myRequest = new Request('https://leak.test/fetch-credentials-include', { credentials: 'include'});
        fetch (myRequest);
    } catch(e) { console.log('fetch-credentials-include: ', e); }
    // - Fetch: GET
    try {
        var myRequest = new Request('https://leak.test/fetch-GET', { method: 'GET'});
        fetch (myRequest);
    } catch(e) { console.log('fetch-GET: ', e); }
    // - Fetch: GET-credentials-include
    try {
        var myRequest = new Request('https://leak.test/fetch-GET-credentials-include', { method: 'GET', credentials: 'include'});
        fetch (myRequest);
    } catch(e) { console.log('fetch-GET-credentials-include: ', e); }
    // - Fetch: HEAD
    try {
        var myRequest = new Request('https://leak.test/fetch-HEAD', { method: 'HEAD'});
        fetch (myRequest);
    } catch(e) { console.log('fetch-HEAD: ', e); }
    // - Fetch: HEAD-credentials-include
    try {
        var myRequest = new Request('https://leak.test/fetch-HEAD-credentials-include', { method: 'HEAD', credentials: 'include'});
        fetch (myRequest);
    } catch(e) { console.log('fetch-HEAD-credentials-include: ', e); }
    // - Fetch: POST
    try {
        var myRequest = new Request('https://leak.test/fetch-POST', { method: 'POST'});
        fetch (myRequest);
    } catch(e) { console.log('fetch-POST: ', e); }
    // - Fetch: POST-credentials-include
    try {
        var myRequest = new Request('https://leak.test/fetch-POST-credentials-include', { method: 'POST', credentials: 'include'});
        fetch (myRequest);
    } catch(e) { console.log('fetch-POST-credentials-include: ', e); }

    event.ports[0].postMessage({
        "message": "Done"
    });
});
