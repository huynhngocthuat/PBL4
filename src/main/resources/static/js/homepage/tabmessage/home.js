let allConversation;
let allUserInConversation;
let allConversationSearch;
let ConversationIdCurrent;

function startTabMessage() {
    fetchConversation();
    moment.locale("vi");
}
startTabMessage();

function fetchConversation() {
    fetchMethod("/conversations")
        .then((res) => {
            allConversation = res;
            allConversationSearch = res;
            renderConversation();
        })
        .catch((err) => {
            console.log(err);
        });
}

function renderConversation() {
    allConversation = allConversation.sort(function (a, b) {
        let dateA = (a.lastMessage === null) ?
            moment(a.updatedAt) : moment(a.lastMessage.createdAt);
        let dateB = (b.lastMessage === null) ?
            moment(b.updatedAt) : moment(b.lastMessage.createdAt);
        let diff = dateB.diff(dateA);
        return diff;
    });
    let listConversations = document.querySelector("#list-conversations");
    listConversations.innerHTML = ``;
    allConversation.map(function (data) {
        if (data.lastMessage !== null) {
            var timeAgo = moment(data.lastMessage.createdAt).fromNow(true);
        }
        let conversationElement = document.createElement("div");
        conversationElement.setAttribute("class", "contact");
        conversationElement.setAttribute("contactID", `${data.id}`);
        conversationElement.setAttribute("onclick", `showListMessage(${data.id})`);
        conversationElement.innerHTML = `
      <div class="contact__avatar">
        <img
        src="${data.urlAvatar}"
        alt="Avatar" />
        <div class="online"></div>
      </div>
      <div class="contact__description">
        <div class="contact__name">
            <p>${data.title === null ? "NULL" : data.title}</p>
        </div>
        <div class="contact__message">
          <p>${data.lastMessage === null ? "" : data.lastMessage.content}</p>
          <span>${timeAgo === undefined ? "" : timeAgo} </span>
        </div>
      </div>
      `;
        listConversations.appendChild(conversationElement);
    });
}

async function showListMessage(idConversation) {
    ConversationIdCurrent = idConversation;

    // HiddenIntro
    hiddenIntro(true);
    // Remove active contact
    let activeContact = document.querySelector(".active-contact");
    if (activeContact !== null) {
        activeContact.classList.remove("active-contact");
    }
    // Add active contact
    let eventTarget = document.querySelector(`[contactID="${idConversation}"`);
    eventTarget.classList.add("active-contact");
    // Set info Chat
    let conversationById = allConversation.find(
        (data) => data.id === idConversation
    );

    allUserInConversation = conversationById.participants;
    setInfoChat(
        conversationById.id,
        conversationById.urlAvatar,
        conversationById.title
    );
    // Set list messages
    let chatBox = document.getElementById("chat-box");
    chatBox.innerHTML = "";
    let messages;
    await fetchMethod(`/conversations/${idConversation}`)
        .then((res) => {
            messages = res;
        })
        .catch((err) => {
            console.log(err)
        })
    messages.map(function (message) {
        loadMessage(message);
    });
    disconnect();
    connect(idConversation);
    let messageInput = document.querySelector("#input-message");
    messageInput.setAttribute("idConversation", idConversation);
}

function setInfoChat(idConversation, urlAvatar, title) {
    // Set title and avatar
    let infoChatBox = document.getElementById("message-tab-main-top");
    infoChatBox.setAttribute("idConversation", idConversation);
    let avatar = infoChatBox.querySelector("img");
    let nameConversation = infoChatBox.querySelector(".main-top__name p");
    avatar.setAttribute("src", urlAvatar);
    nameConversation.textContent = title;
}

function hiddenIntro(isHidden) {
    if (isHidden) {
        let mainIntro = document.querySelector(".main-intro");
        mainIntro.classList.add("main-intro-hidden");
    } else {
        let mainIntro = document.querySelector(".main-intro");
        mainIntro.classList.remove("main-intro-hidden");
    }
}

function searchConversations() {
    let inputValue = this.event.target.value;
    if (inputValue) {
        let reSearch = new RegExp(inputValue, 'ig');
        allConversation = allConversationSearch.filter(function (e) {
            return e.title.search(reSearch) != -1
        })
    } else {
        allConversation = allConversationSearch
    }
    renderConversation();
}

