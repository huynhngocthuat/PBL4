let stompClient = null;
let phoneNumber = null;

function connect(idConversation) {
    let socket = new SockJS('/gkz-stomp-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        // onConnected func
        stompClient.subscribe('/topic/public/' + idConversation, function (createMessageDto) {

            let message = JSON.parse(createMessageDto.body); // Đối tượng Json
            console.log(message);
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

    if (messageContent && stompClient) {
        let createMessageDto = {
            content: messageInput.value,
            conversationId: ConversationIdCurrent,
            createdAt : null,
        };
        stompClient.send("/app/send", {}, JSON.stringify(createMessageDto));
        messageInput.value = '';
    }
}

function sendAttachments(){
    const realFileBtn = document.getElementById("uploadFiles");
    realFileBtn.click();
    realFileBtn.addEventListener("change", function (event){
        if(realFileBtn.value && stompClient){
            const file = event.target.files;
            console.log(file.size)
            if (file.size < 524288)
            {
                console.log("File duowis 512")
            }
            else {
                console.log("file tren 512")
            }
            let formData = new FormData(); // Currently empty
            formData.append("conversationId", ConversationIdCurrent);
            formData.append("uploadFiles", file);

            let listCreateAttachmentMessageDto =
                fetchMethodFormData('/saveAttachment', formData, 'POST');
            console.log(listCreateAttachmentMessageDto);
            // Gửi list message qua websocket
            // for (let createAttachmentMessageDto in listCreateAttachmentMessageDto){
            //     stompClient.send("/app/sendAttachments", {}, JSON.stringify(createAttachmentMessageDto));
            // }
        }
    });
}

function disconnect() {
    if (this.stompClient != null) {
        this.stompClient.disconnect();
    }
    console.log('Disconnected!');
}