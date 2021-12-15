function messageNewElementType1(msg, type) {
    const message = document.createElement("div");
    message.className = "message-group my-chat";
    message.setAttribute("idSender", msg.userId);
    let messageLi = messageConcatLiType1(msg, type);
    message.innerHTML = `
  <div class="list-messages">
    <ul>
        <li>
          ${messageLi.innerHTML}
        </li>
    </ul>
  </div>
    `;
    return message;
}

function messageConcatLiType1(msg, type) {
    let message = document.createElement("li");
    switch (type) {
        case "image": {
            message.innerHTML = `
            <img 
              src="${BASE_URL}/getAttachment/${msg.attachmentId}"
              ondblclick="openImage('${BASE_URL}/downloadAttachment/${msg.attachmentId}')"
            > 
          `
            break;
        }
        case "message": {
            message.innerHTML = `
            <p> ${msg.content} </p>
          `
            break;
        }
        case "file": {
            message.innerHTML = `
            <a 
            href="${BASE_URL}/downloadAttachment/${msg.attachmentId}"
            target="_blank"
            rel="bookmark"
            > ${msg.content} </a>
          `
            break;
        }
    }
    message.innerHTML += `
    <div class="message-sent-time">${msg.createdAt}</div>
    <div class="message-status">
      <i class="far fa-check-circle"></i>
    </div>
  `
    return message;
}

function messageNewElementType2(msg, name, urlAvatar, type) {
    const message = document.createElement("div");
    message.className = "message-group your-chat";
    message.setAttribute("idSender", msg.userId);
    let messageLi = messageConcatLiType2(msg, type);
    message.innerHTML = ` 
    <div class="message_avatar">
      <img src=${urlAvatar} alt="avatar"/>
    </div>
    <div class="message-main">
      <div class="message__info">
        <p class="message__name">${name}</p>
      </div>
    <div class="list-messages" id="list-messages">
      <ul>
          <li>
              ${messageLi.innerHTML}
          </li>
      </ul>
    </div>
  </div>
  `;
    return message;
}

function messageConcatLiType2(msg, type) {
    let message = document.createElement("li");
    switch (type) {
        case "image": {
            message.innerHTML = `
            <img 
              src="${BASE_URL}/getAttachment/${msg.attachmentId}"
              ondblclick="openImage('${BASE_URL}/downloadAttachment/${msg.attachmentId}')"
            > 
            `
            break;
        }
        case "message": {
            message.innerHTML = `
            <p> ${msg.content} </p>
          `
            break;
        }
        case "file": {
            message.innerHTML = `
            <a 
            href="${BASE_URL}/downloadAttachment/${msg.attachmentId}"
            target="_blank"
            rel="bookmark"
            > ${msg.content} </a>
          `
            break;
        }
    }
    message.innerHTML += `
    <div class="message-sent-time">${msg.createdAt}</div>
  `
    return message;
}

function openImage(link) {
    window.open(link, '_blank');
}

function loadMessage(msg) {
    let messageDiv;
    msg.createdAt = moment(msg.createdAt).format('DD/MM/YY, HH:mm:ss');
    let idUserLogin = dataLogin.getID();

    let chatBox = document.getElementById("chat-box");
    let lastChildChatBox = chatBox.lastElementChild;

    if (chatBox.innerHTML === "" || lastChildChatBox.getAttribute("idSender") != msg.userId) {
        if (msg.userId == idUserLogin) {
            messageDiv = messageNewElementType1(msg, getTypeMessage(msg))
        } else {
            let infoUserSendMsg = allUserInConversation.find((x) => x.userId === msg.userId);
            let name = infoUserSendMsg.userFirstName + infoUserSendMsg.userLastName;
            let urlAvatar = infoUserSendMsg.userUrlAvatar;
            messageDiv = messageNewElementType2(msg, name, urlAvatar, getTypeMessage(msg));
        }
        chatBox.appendChild(messageDiv);
    } else {
        let ulLastChild = lastChildChatBox.querySelector("ul");
        let messageLi =
            msg.userId == idUserLogin ?
                messageConcatLiType1(msg, getTypeMessage(msg)) :
                messageConcatLiType2(msg, getTypeMessage(msg));
        ulLastChild.appendChild(messageLi);
    }
}

function getTypeMessage(msg) {
    if (msg.attachmentId) {
        if (msg.attachmentFileType.includes("image")) {
            return "image";
        } else {
            return "file";
        }
    } else {
        return "message";
    }
}