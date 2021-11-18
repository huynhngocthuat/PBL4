function messageNewElementType1(msg) {
    const message = document.createElement("div");
    message.className = "message-group my-chat";
    message.setAttribute("userID", msg.userId);
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

function messageNewElementType2(msg, title, url) {
    let infoChatBox = document.getElementById("main-top");
    let urlAvatar = infoChatBox.querySelector("img").getAttribute("src");
    let nameConversation = infoChatBox.querySelector(".main-top__name p").textContent;
    const message = document.createElement("div");
    message.className = "message-group your-chat";
    message.setAttribute("userID", msg.userId);
    message.innerHTML = ` 
      <div class="message_avatar">
        <img src=${urlAvatar} alt="avatar"/>
      </div>
      <div class="message-main">
        <div class="message__info">
          <p class="message__name">${nameConversation}</p>
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

function loadMessage(msg, title, url) {
    var userID = dataLogin.getID();
    var chatBox = document.getElementById("chat-box");
    if (chatBox.innerHTML === "") {
        let messageDiv =
            msg.userId == userID ?
                messageNewElementType1(msg) :
                messageNewElementType2(msg, title, url);
        chatBox.appendChild(messageDiv);
    } else {
        let lastChildChatBox = chatBox.lastElementChild;
        if (lastChildChatBox.getAttribute("userID") == msg.userId) {
            var ulLastChild = lastChildChatBox.querySelector("ul");
            let messageLi =
                msg.userId == userID ?
                    messageConcatLiType1(msg) :
                    messageConcatLiType2(msg);
            ulLastChild.appendChild(messageLi);
        } else {
            var messageDiv =
                msg.userId == userID ?
                    messageNewElementType1(msg) :
                    messageNewElementType2(msg);
            chatBox.appendChild(messageDiv);
        }
    }
}