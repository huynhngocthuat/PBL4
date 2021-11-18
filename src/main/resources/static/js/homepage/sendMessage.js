var stompClient = null;
var phoneNumber = null;

function connect(idConversation) {
    var socket = new SockJS('/gkz-stomp-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        // onConnected func
        stompClient.subscribe('/topic/public/' + idConversation, function (createMessageDto) {
            var message = JSON.parse(createMessageDto.body); // Đối tượng Json
            loadMessage(message, "Huynh Ngoc Thuat", "https://images.unsplash.com/photo-1508243771214-6e95d137426b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80")
        });
    }, onError);
}

function onError(error) {
    alert('Could not connect to WebSocket server. Please refresh this page to try again!');
}

function enterSend(e) {
    if (e.key === 'Enter' || e.keyCode === 13) {
        send();
    }
}

function send() {
    let messageInput = document.querySelector("#input-message");
    let messageContent = messageInput.value.trim();
    let conversationId = messageInput.getAttribute("idConversation");
    if (messageContent && stompClient) {
        var createMessageDto = {
            userId: dataLogin.getID(),
            content: messageInput.value,
            conversationId: conversationId,
            createdAt : null,
        };
        stompClient.send("/app/send", {}, JSON.stringify(createMessageDto));
        messageInput.value = '';
    }
}

function disconnect() {
    if (this.stompClient != null) {
        this.stompClient.disconnect();
    }
    console.log('Disconnected!');
}