let stompClient = null;
let phoneNumber = null;

function connect(idConversation) {
    let socket = new SockJS('/gkz-stomp-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        // onConnected func
        stompClient.subscribe('/topic/public/' + idConversation, function (createMessageDto) {
            let message = JSON.parse(createMessageDto.body); // Đối tượng Json
            loadMessage(message)
            message.createdAt = moment(message.createdAt, "DD/MM/YY, HH:mm:ss").format("YYYY-MM-DD HH:mm:ss.0")
            let listConversations = document.querySelector("#list-conversations");
            listConversations.innerHTML = ``;
            let conversationContainMsg = allConversation.find(
                (data) => data.id === message.conversationId
            )
            conversationContainMsg.lastMessage = message;
            renderConversation();
        });
    }, onError);
}

function onError(error) {
    alert('Could not connect to WebSocket server. Please refresh this page to try again!');
}

function CheckEnterToSend() {
    if (this.event.code === "Enter") {
        send();
    }
}

function send() {
    let messageInput = document.querySelector("#input-message");
    let messageContent = messageInput.value.trim();
    let conversationId = messageInput.getAttribute("idConversation");
    if (messageContent && stompClient) {
        let createMessageDto = {
            userId: dataLogin.getID(),
            content: messageInput.value,
            conversationId: conversationId,
            createdAt : null,
        };
        stompClient.send("/app/send", {}, JSON.stringify(createMessageDto));
        messageInput.value = '';
    }
}

function sendAttachments(){
    const realFileBtn = document.getElementById("uploadFiles");
    realFileBtn.click();
    realFileBtn.addEventListener("change", function (){
        if(realFileBtn.value && stompClient){
            // Lưu mảng file được select và trả về list createAttachmentMessageDto
            // Tham số đầu vào Multipartfile, conversationId, userId
            let listCreateAttachmentMessageDto = fetchMethod('/saveAttachment', method ='POST')
            // Gửi list message qua websocket
            for (let createAttachmentMessageDto in listCreateAttachmentMessageDto){
                stompClient.send("/app/sendAttachments", {}, JSON.stringify(createAttachmentMessageDto));
            }
        }
    });
}

function disconnect() {
    if (this.stompClient != null) {
        this.stompClient.disconnect();
    }
    console.log('Disconnected!');
}