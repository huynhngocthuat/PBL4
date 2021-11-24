let allConversation;
let allUserInConversation;
function start() {
    fetchConversation();
    moment.locale("vi");
    console.log(dataLogin.getID())
}
start();

function fetchConversation() {
    fetchMethod("/conversations").then((data) => {
        console.log(data)
        allConversation = data.sort(function (a, b) {
            let dateA = moment(a.lastMessage.createdAt);
            let dateB = moment(b.lastMessage.createdAt);
            let diff = dateB.diff(dateA);
            return diff;
        });
        renderConversation();
    });
}

function renderConversation() {
    let listContacts = document.querySelector("#list-contacts");

    allConversation.map(function (data) {
        if (data.lastMessage !== null) {
            var time = moment(data.lastMessage.createdAt).format(
                "MMMM Do YYYY, h:mm:ss a"
            );
            var timeAgo = moment(time, "MMMM Do YYYY, h:mm:ss a").fromNow(true);
        }
        let contactDiv = document.createElement("div");
        contactDiv.setAttribute("class", "contact");
        contactDiv.setAttribute("contactID", `${data.id}`);
        contactDiv.setAttribute("onclick", `showListMessage(${data.id})`);
        contactDiv.innerHTML = `
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
        listContacts.appendChild(contactDiv);
    });
}

async function showListMessage(idConversation) {
    console.log(idConversation)
    console.log(2222)
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
    console.log(allUserInConversation)
    setInfoChat(
        conversationById.idConversation,
        conversationById.urlAvatar,
        conversationById.title
    );

    // Set list messages
    let chatBox = document.getElementById("chat-box");
    chatBox.innerHTML = "";
    let messages = await fetchMethod(`/conversations/${idConversation}`);
    messages.map(function (message) {
        loadMessage(message);
    });
    disconnect();
    connect(idConversation);
    var messageInput = document.querySelector("#input-message");
    messageInput.setAttribute("idConversation", idConversation);
}

function setInfoChat(idConversation, urlAvatar, title) {
    // Set title and avatar
    let infoChatBox = document.getElementById("main-top");
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