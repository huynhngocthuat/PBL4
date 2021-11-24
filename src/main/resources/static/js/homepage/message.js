function messageNewElementType1(msg) {
    const message = document.createElement("div");
    message.className = "message-group my-chat";
    message.setAttribute("idSender", msg.userId);
    message.innerHTML = `
    <div class="list-messages">
      <ul>
          <li>
              <p>
                  ${msg.content}
              </p>
              <div class="message-sent-time">${msg.createdAt}</div>
              <div class="message-status">
                <i class="far fa-check-circle"></i>
              </div>
          </li>
      </ul>
    </div>
      `;
    return message;
}

function messageConcatLiType1(msg) {
    const message = document.createElement("li");
    message.innerHTML = `
      <p>
          ${msg.content}   
      </p>
      <div class="message-sent-time">${msg.createdAt}</div>
      <div class="message-status">
                <i class="far fa-check-circle"></i>
      </div>
      `;
    return message;
}

function messageNewElementType2(msg, name, urlAvatar) {
    const message = document.createElement("div");
    message.className = "message-group your-chat";
    message.setAttribute("idSender", msg.userId);
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
            <p>
              ${msg.content}
            </p>
            <div class="message-sent-time">${msg.createdAt}</div>
        </ul>
      </div>
    </div>
    `;
    return message;
}

function messageConcatLiType2(msg) {
    const message = document.createElement("li");
    message.innerHTML = `
      <p>
          ${msg.content}   
      </p>
      <div class="message-sent-time">${msg.createdAt}</div>
      `;
    return message;
}

function loadMessage(msg) {

    let messageDiv;
    msg.createdAt = moment(msg.createdAt).format('DD/MM/YY, HH:mm:ss');
    let idUserLogin = dataLogin.getID();

    let chatBox = document.getElementById("chat-box");
    let lastChildChatBox = chatBox.lastElementChild;
    console.log("---------------------------------------");
    console.log(msg)
    console.log(idUserLogin)
    console.log(msg.userId)

    if (chatBox.innerHTML === "" || lastChildChatBox.getAttribute("idSender") != msg.userId) {
        if (msg.userId == idUserLogin) {
            messageDiv = messageNewElementType1(msg)
        }
        else {
            let infoUserSendMsg = allUserInConversation.find((x) => x.userId === msg.userId)
            console.log(infoUserSendMsg)
            console.log("---------------------------------------");
            let name = infoUserSendMsg.userFirstName + infoUserSendMsg.userLastName;
            let urlAvatar = infoUserSendMsg.userUrlAvatar;
            messageDiv = messageNewElementType2(msg, name, urlAvatar);
        }
        chatBox.appendChild(messageDiv);
    } else {
        let ulLastChild = lastChildChatBox.querySelector("ul");
        let messageLi =
            msg.userId == idUserLogin ?
                messageConcatLiType1(msg) :
                messageConcatLiType2(msg);
        ulLastChild.appendChild(messageLi);
    }
}