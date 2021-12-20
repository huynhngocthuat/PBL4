let stompClient = null;
function connect(idConversation) {
    let socket = new SockJS('/gkz-stomp-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        if (idConversation !== 0)
        {
            stompClient.subscribe('/topic/public/' + idConversation, function (createMessageDto) {
                let message = JSON.parse(createMessageDto.body); // Đối tượng Json
                if (message.conversationId === ConversationIdCurrent)
                {
                    loadMessage(message);
                    let mainChatBox = document.getElementById("chat-box");
                    mainChatBox.scrollBy(0, mainChatBox.scrollHeight);
                }
            });
        }

        stompClient.subscribe('/topic/public/global', function (lastMessage) {
            let message = JSON.parse(lastMessage.body); // Đối tượng Json
            let dateFormat = moment(message.createdAt).format("YYYY-MM-DD HH:mm:ss.0");
            message.createdAt = dateFormat;
            let conversationContainMsg = allConversation.find(
                (data) => data.id === message.conversationId
            );
            conversationContainMsg.lastMessage = message;
            renderConversation();
        });
    }, onError);
}

function onError(error) {
    console.log(error);
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
            createdAt: null,
        };
        stompClient.send("/app/send", {}, JSON.stringify(createMessageDto));
        messageInput.value = '';
    }
}

function openFileSelectInput(idInput) {
    const realFileBtn = document.getElementById(idInput);
    realFileBtn.click();
}

function sendAttachments() {
    let selectFileBtn = this.event.target;
    if (selectFileBtn.value && stompClient) {
        const files = selectFileBtn.files;
        let formData = new FormData(); // Currently empty
        let sendOk = false;
        for (let i = 0; i < files.length; i++) {
            if (files[i].size < 512000) {
                formData.append("uploadFiles", files[i]);
                sendOk = true;
            } else {
                console.error(`${files[i].name} vượt quá kích thước cho phép`);
            }
        }
        formData.append("conversationId", ConversationIdCurrent);
        if (sendOk === true) {
            fetchMethodFormData('/sendAttachment', formData, 'post')
                .then(res => {})
                .catch(err => console.log(err));
        }
    }
}


function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
        console.log('Disconnected!');
    }
}