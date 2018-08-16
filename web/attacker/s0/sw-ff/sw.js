self.addEventListener('install', event => {
    console.log("being installed");
event.registerForeignFetch({
    scopes: [self.registration.scope], // or some sub-scope
    origins: ['*'] // or ['https://example.com']
});
});

self.addEventListener('activate', event => {
    console.log("activate");
    return self.clients.claim();
});

self.addEventListener('foreignfetch', event => {
    console.log("caught foreign fetch");
// Testing potential leaks
// - Import scripts
try {
    importScripts('https://adition.com/import-scripts')
} catch(e) { console.log('import-scripts: ', e); }
// - Fetch:
try {
    var myRequest = new Request('https://adition.com/fetch', { mode: 'no-cors'});
    fetch(myRequest);
} catch(e) { console.log('fetch: ', e); }
// - Fetch: credentials-include
try {
    var myRequest = new Request('https://adition.com/fetch-credentials-include', { mode: 'no-cors',credentials: 'include'});
    fetch (myRequest);
} catch(e) { console.log('fetch-credentials-include: ', e); }
// - Fetch: GET
try {
    var myRequest = new Request('https://adition.com/fetch-GET', { mode: 'no-cors',method: 'GET'});
    fetch (myRequest);
} catch(e) { console.log('fetch-GET: ', e); }
// - Fetch: GET-credentials-include
try {
    var myRequest = new Request('https://adition.com/fetch-GET-credentials-include', { mode: 'no-cors',method: 'GET', credentials: 'include'});
    fetch (myRequest);
} catch(e) { console.log('fetch-GET-credentials-include: ', e); }
// - Fetch: HEAD
try {
    var myRequest = new Request('https://adition.com/fetch-HEAD', { mode: 'no-cors',method: 'HEAD'});
    fetch (myRequest);
} catch(e) { console.log('fetch-HEAD: ', e); }
// - Fetch: HEAD-credentials-include
try {
    var myRequest = new Request('https://adition.com/fetch-HEAD-credentials-include', { mode: 'no-cors',method: 'HEAD', credentials: 'include'});
    fetch (myRequest);
} catch(e) { console.log('fetch-HEAD-credentials-include: ', e); }
// - Fetch: POST
try {
    var myRequest = new Request('https://adition.com/fetch-POST', { mode: 'no-cors',method: 'POST'});
    fetch (myRequest);
} catch(e) { console.log('fetch-POST: ', e); }
// - Fetch: POST-credentials-include
try {
    var myRequest = new Request('https://adition.com/fetch-POST-credentials-include', { mode: 'no-cors',method: 'POST', credentials: 'include'});
    fetch (myRequest);
} catch(e) { console.log('fetch-POST-credentials-include: ', e); }


function send(method, url) {
    try {
        var xhr = new XMLHttpRequest();
        xhr.open(method, url);
        xhr.send();

        xhr = new XMLHttpRequest();
        xhr.open(method, url + "-withCredentials")
        xhr.withCredentials = true;
        xhr.send();
    } catch (err) {
        console.log(err);
    }
}

send('GET', 'https://adition.com/xhr-get');
send('HEAD', 'https://adition.com/xhr-head');
send('POST', 'https://adition.com/xhr-post');
send('PUT', 'https://adition.com/xhr-put');
send('DELETE', 'https://adition.com/xhr-delete');

try {
    navigator.sendBeacon("https://adition.com/send-beacon");
} catch (err) {
    console.log(err);
}

try {
    var eSource = new EventSource('https://adition.com/event-source');
} catch (err) {
    console.log(err);
}

console.log("going to respondWith");
if (!event.request.url.includes("ad.js"))
    return;
console.log("Going to fetch:" + event.request.url);

event.respondWith(
    fetch("https://adition.com/refetch-through-innocent-script.js", {
        mode: 'no-cors',
        credentials: 'include'
    }).then(response => {
        return {
            response: response
        };
})
    );
});