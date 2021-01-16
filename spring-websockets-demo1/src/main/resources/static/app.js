window.addEventListener('DOMContentLoaded', (event) => {
    // let sockJs = new SockJS('/gs-guide-websocket');
    let sockJs = new SockJS('/gs-guide-websocket');
    let stompClient = Stomp.over(sockJs);
    stompClient.connect('', '', (frame) => {
        console.log(`Connected to ${frame}`);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            console.log(`Got greeting: ${greeting}`);

            let el = document.querySelector("#messages > tbody");
            console.log('Got', el);
            let tr = document.createElement('tr');
            let td1 = document.createElement('td');
            let td2 = document.createElement('td');
            let message = JSON.parse(greeting.body);
            td1.textContent = new Date().toISOString();
            td2.textContent = message.content;

            tr.append(td1);
            tr.append(td2);
            el.append(tr);
        });
    });

    document.querySelector('#submit-message')
        .addEventListener('click', (e) => {
            let messageContentNode = document.querySelector('#message-content');
            let messageValue = messageContentNode.value;
            messageContentNode.value = '';
            stompClient.send('/app/hello', {}, JSON.stringify({
                name: messageValue
            }));
        });
});
