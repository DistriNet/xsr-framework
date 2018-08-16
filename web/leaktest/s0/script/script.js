function test() {
    try {
        const socket1 = new WebSocket('ws://adition.com/report/?leak=websocket-ws:9030');
        socket1.addEventListener('open', e => socket1.send('The socket has been opened'));
        //socket1.close();
    } catch(err) {
    console.log(err);
    }

try {
    const socket2 = new WebSocket('wss://adition.com/report/?leak=websocket-wss:9030');
    socket2.addEventListener('open', e => socket2.send('The socket has been opened'));
    //socket2.close();
} catch (err) {
    console.log(err);
}

try {
    var myRequest = new Request('https://adition.com/report/?leak=fetch');
    fetch(myRequest);
} catch (e) {
    console.log('fetch: ', e);
}
// - Fetch: credentials-include
try {
    var myRequest = new Request('https://adition.com/report/?leak=fetch-credentials-include', {credentials: 'include'});
    fetch(myRequest);
} catch (e) {
    console.log('fetch-credentials-include: ', e);
}
// - Fetch: GET
try {
    var myRequest = new Request('https://adition.com/report/?leak=fetch-GET', {method: 'GET'});
    fetch(myRequest);
} catch (e) {
    console.log('fetch-GET: ', e);
}
// - Fetch: GET-credentials-include
try {
    var myRequest = new Request('https://adition.com/report/?leak=fetch-GET-credentials-include', {
        method: 'GET',
        credentials: 'include'
    });
    fetch(myRequest);
} catch (e) {
    console.log('fetch-GET-credentials-include: ', e);
}
// - Fetch: HEAD
try {
    var myRequest = new Request('https://adition.com/report/?leak=fetch-HEAD', {method: 'HEAD'});
    fetch(myRequest);
} catch (e) {
    console.log('fetch-HEAD: ', e);
}
// - Fetch: HEAD-credentials-include
try {
    var myRequest = new Request('https://adition.com/report/?leak=fetch-HEAD-credentials-include', {
        method: 'HEAD',
        credentials: 'include'
    });
    fetch(myRequest);
} catch (e) {
    console.log('fetch-HEAD-credentials-include: ', e);
}
// - Fetch: POST
try {
    var myRequest = new Request('https://adition.com/report/?leak=fetch-POST', {method: 'POST'});
    fetch(myRequest);
} catch (e) {
    console.log('fetch-POST: ', e);
}
// - Fetch: POST-credentials-include
try {
    var myRequest = new Request('https://adition.com/report/?leak=fetch-POST-credentials-include', {
        method: 'POST',
        credentials: 'include'
    });
    fetch(myRequest);
} catch (e) {
    console.log('fetch-POST-credentials-include: ', e);
}

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

send('GET', 'https://adition.com/report/?leak=xhr-get');
send('HEAD', 'https://adition.com/report/?leak=xhr-head');
send('POST', 'https://adition.com/report/?leak=xhr-post');
send('PUT', 'https://adition.com/report/?leak=xhr-put');
send('DELETE', 'https://adition.com/report/?leak=xhr-delete');

try {
    navigator.sendBeacon("https://adition.com/report/?leak=send-beacon");
} catch (err) {
    console.log(err);
}

try {
    var eSource = new EventSource('https://adition.com/report/?leak=event-source');
} catch (err) {
    console.log(err);
}

try {
    var eSource = new EventSource('https://adition.com/report/?leak=event-source-credentials-include', {withCredentials : true});
} catch (err) {
    console.log(err);
}

try {
    //# sourceMappingURL=https://adition.com/source-map
} catch (err) {
    console.log(err);
}

return new Promise(function (resolve, reject) {
    resolve();
});

}

function run() {

    test().then(setTimeout(end, 2000));
}

function end() {
    try {
        var elem = document.getElementById('check');
        elem.innerHTML = 'Done';
    } catch (err) {
        console.log(err);
    }
}


run();