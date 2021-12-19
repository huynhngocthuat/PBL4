let allContacts;
let allContactsSearch;
let allFriendRequests;
let allGroupConversations;

function startTabContact() {
    fetchContacts();
    fetchContactsPending();
    moment.locale("vi");
}

function fetchContacts() {
    fetchMethod("/contacts")
        .then((res) => {
            allContacts = res;
            allContactsSearch = res;
            renderContacts(allContacts);
        })
        .catch((err) => {
            console.log(err);
        });
}

function renderContacts(contacts) {
    let listContacts = document.querySelector("#list-contacts");
    listContacts.innerHTML = "";
    contacts.map((contact) => {
        let contactDiv = document.createElement("div");
        contactDiv.setAttribute("class", "contact-item");
        contactDiv.setAttribute("contact-id", `${contact.id}`);
        contactDiv.innerHTML = `
            <img class="contact-item__avatar" src="${
            contact.urlAvatar
        }" alt="avatar">
            <span class="contact-item__name">
                ${contact.firstName + " " + contact.lastName} 
            </span>
            
        `;
        if (contact.id !== dataLogin.getID()) {
            contactDiv.innerHTML += `
            <div class="contact-item__action">
              <div class="dropbtn">
                <i class="fas fa-ellipsis-h"></i>
              </div>    
              <div class="dropdown-content">
                <p onclick="deleteContact(${contact.id})">Xóa bạn</p>
                <p>Xem thông tin</p>
              </div>
            </div>
            `;
        }
        listContacts.append(contactDiv);
    });
}

function deleteContact(idContact) {
    fetchMethod(`/contacts/delete/${idContact}`, {}, "delete")
        .then((res) => {
            if (res.status === 204) {
                allContacts = allContacts.filter((data) => data.id != idContact);
                renderContacts(allContacts);
                alert("Xóa thành công");
            } else {
                console.log(res);
            }
        })
        .catch((err) => {
            console.log(err);
        });
}

function fetchGroupConversations() {
    fetchMethod("/contacts/groups")
        .then((res) => {
            allGroupConversations = res;
            renderGroupConversations(allGroupConversations);
        })
        .catch((err) => {
            console.log(err);
        });
}

// Friend Request
function fetchContactsPending() {
    fetchMethod("/contacts/pending")
        .then((res) => {
            allFriendRequests = res;
            renderFriendRequests(allFriendRequests);
        })
        .catch((err) => {
            console.log(err);
        });
}

function renderFriendRequests(friendRequests) {
    let listFriendRequests = document.querySelector("#list-friend-requests");
    listFriendRequests.innerHTML = "";
    friendRequests.map((friendRequest) => {
        let friendRequestDiv = document.createElement("div");
        friendRequestDiv.setAttribute("class", "friend-request-item");
        friendRequestDiv.setAttribute("friend-request-id", `${friendRequest.id}`);
        friendRequestDiv.innerHTML = `
         <div class="friend-request-item__avatar">
            <img src="${friendRequest.urlAvatar}" alt="avatar">
         </div>
         <div class="friend-request-item__info">
            <p class="friend-request-item__name">
                ${friendRequest.firstName + " " + friendRequest.lastName}
            </p>
            <p class="friend-request-item__message">
                ${friendRequest.invitationMessage}
            </p>
         </div>
         <div class="friend-request-item__action">
                <div class="btn btn-secondary" onclick="declineRequest(${friendRequest.id})">
                    Bỏ qua
                </div>
                <div class="btn btn-primary" onclick="acceptRequest(${friendRequest.id})">
                    Đồng ý
                </div>
         </div>
         
        `;
        listFriendRequests.append(friendRequestDiv);
    });
}

function acceptRequest(idContact) {
    // acceptContactInvitation
    fetchMethod(`/contacts/accept/${idContact}`, {}, "post")
        .then((res) => {
            if (res.status === 204) {
                allFriendRequests = allFriendRequests.filter(
                    (data) => data.id !== idContact
                );
                let friendRequestDiv = document.querySelector(
                    `[friend-request-id="${idContact}"]`
                );
                friendRequestDiv.remove();
                fetchContacts();
            } else {
                console.log(res);
            }
        })
        .catch((err) => {
            console.log(err);
        });
}

function declineRequest(idContact) {
    // declineContactInvitation
    fetchMethod(`/contacts/decline/${idContact}`, {}, "delete")
        .then((res) => {
            if (res.status === 204) {
                allFriendRequests = allFriendRequests.filter(
                    (data) => data.id !== idContact
                );
                let friendRequestDiv = document.querySelector(
                    `[friend-request-id="${idContact}"]`
                );
                friendRequestDiv.remove();
            } else {
                console.log(res);
            }
        })
        .catch((err) => {
            console.log(err);
        });
}

function renderGroupConversations(GroupConversations) {
    let listGroupConversations = document.querySelector("#list-groups");
    listGroupConversations.innerHTML = "";
    GroupConversations.map((GroupConversation) => {
        let GroupConversationDiv = document.createElement("div");
        GroupConversationDiv.setAttribute("class", "group-item");
        GroupConversationDiv.setAttribute("group-id", `${GroupConversation.id}`);
        GroupConversationDiv.setAttribute(
            "onclick",
            `membersInGroup(${GroupConversation.id})`
        );
        GroupConversationDiv.innerHTML = `
         <img src="${
            GroupConversation.urlAvatar
        }" alt="avatar" class="group-item__avatar">
         <p class=group-item__name">
            ${GroupConversation.title}
        </p>
         <p className="group-item__info">
            ${GroupConversation.participants.length + " "} thành viên
         </p>
        `;
        listGroupConversations.append(GroupConversationDiv);
    });
}

function switchAction(actionClickTab) {
    let actionActiveTab = document.querySelector(
        ".action-item.action-item-active"
    );
    if (actionActiveTab !== actionClickTab) {
        actionActiveTab.classList.remove("action-item-active");
        actionClickTab.classList.add("action-item-active");
        return true;
    } else {
        return false;
    }
}

function openFriendRequestList() {
    let actionClickTab = this.event.target;
    let friendRequestListElement = document.querySelector("#friend-request-list");
    let groupListElement = document.querySelector("#group-list");

    if (switchAction(actionClickTab)) {
        groupListElement.classList.remove("active-block");
        friendRequestListElement.classList.add("active-block");
        fetchContactsPending();
    }
}

function openGroupList() {
    let actionClickTab = this.event.target;
    let friendRequestListElement = document.querySelector("#friend-request-list");
    let groupListElement = document.querySelector("#group-list");

    if (switchAction(actionClickTab)) {
        groupListElement.classList.add("active-block");
        friendRequestListElement.classList.remove("active-block");
        fetchGroupConversations();
    }
}

function membersInGroup(idConversations) {
    let conversations = allGroupConversations.find(
        (data) => (data.id === idConversations)
    );
    let members = conversations.participants;
    let creatorId = conversations.creatorId;

    let overlayElement = document.querySelector(
        `[action-overlay-name="members-in-group"]`
    );
    overlayElement.classList.add("active-flex");

    // Show list members
    let listMembersDiv = document.querySelector("#list-members");
    listMembersDiv.innerHTML = "";
    members.map((member) => {
        let memberDiv = document.createElement("div");
        memberDiv.setAttribute("class", "list-members-item");
        memberDiv.innerHTML = `
         <img src="${
            member.urlAvatar
        }" alt="avatar" class="list-members-item__avatar">
         <span class="list-members-item__name">
            ${member.firstName + " " + member.lastName}                       
         </span>
         `;

        if (creatorId === member.id) {
            memberDiv.innerHTML += `<span class="list-members-item__role"> Quản trị viên </span>`;
            listMembersDiv.insertBefore(memberDiv, listMembersDiv.firstChild);
        } else {
            listMembersDiv.append(memberDiv);
        }
    });

    let btnCancel = overlayElement.querySelector(".btn-cancel");
    btnCancel.onclick = function () {
        overlayElement.classList.remove("active-flex");
    };
}
function openCreateGroup() {
    let overlayElement = document.querySelector("#overlay-create-group");
    overlayElement.classList.add("active-flex");
    let btnSkip = overlayElement.querySelector(".btn.btn-secondary");
    renderContactInCreateGroup(allContacts);

    btnSkip.onclick = function () {
        let createGroupList = document.querySelector("#create-group-list");
        createGroupList.innerHTML = "";
        overlayElement.classList.remove("active-flex");
    };
}

function renderContactInCreateGroup(contacts) {
    let createGroupList = document.querySelector("#create-group-list");
    contacts.map((contact, index) => {
        let itemElement = document.createElement("div");
        itemElement.setAttribute("class", "create-group__list-item");
        itemElement.innerHTML = `
            <div class="item__checkbox">
                <input type="checkbox" 
                        id="create-group-id-user-${index+1}" 
                        class="checkbox-input"
                        value="${contact.id}" 
                        ${contact.id === dataLogin.getID() ? "disabled checked = true name='userLogin'"  : "name='user'"} >
                <label for='create-group-id-user-${index+1}' class="checkbox-label">
                    <i class="fa fa-check"></i>
                </label>
            </div>
            <div class="item__info">
                <img src="${contact.urlAvatar}"
                     alt="avatar" class="item__avatar">
                <p class="item__name">
                    ${contact.firstName + " " + contact.lastName}
                </p>
            </div>
        `;
        if (contact.id === dataLogin.getID())
        {
            createGroupList.prepend(itemElement);
        }
        else {
            createGroupList.appendChild(itemElement);
        }
    })
}

function createConversation() {
    let nameConversation = document.querySelector("#name-group-create").value;
    let urlAvatar = document.querySelector("#url-avatar-group-create").value;
    let createGroupList = document.querySelector("#create-group-list");
    let listInputChecked = createGroupList.querySelectorAll('input[name="user"]:checked');
    let listIdUser = [];
    listInputChecked.forEach((item) => {
        listIdUser.push(item.value);
    });
    if (nameConversation === "")
    {
        alert("Tên nhóm trống");
        return;
    }
    if (urlAvatar === "")
    {
        alert("Url avatar trống");
        return;
    }
    if (listIdUser.length === 0)
    {
        alert("Hãy chọn người bạn muốn thêm vào nhóm chat");
        return;
    }
    if (listIdUser.length < 2)
    {
        alert("Chọn tối thiểu 3 người");
        return;
    }

    let formdata = new FormData();
    formdata.append("conversationTitle", nameConversation);
    formdata.append("conversationAvatar", urlAvatar);
    formdata.append("participantIDs", listIdUser);

    fetchMethodFormData("/conversations/create", formdata, "post")
        .then(res => {
            if (res.status === 200)
            {
                alert("Tạo nhóm thành công");
                let btnSkip = document.querySelector(".create-group__action .btn.btn-secondary");
                btnSkip.click();
                fetchGroupConversations();
            }
            if (res.status === 500)
            {
                alert("Tạo nhóm không thành công");
            }
        })
        .catch(err => {
            console.log(err);
        })
}

// Send request friend
let idContactFriendRequest = -1;
function openAddFriend() {
    let overlayElement = document.querySelector("#overlay-add-friend");
    overlayElement.classList.add("active-flex");
    let btnSkip = overlayElement.querySelector(".btn.btn-secondary");
    let btnSendRequestFriend = document.querySelector("#btn-send-request-friend");

    btnSkip.onclick = function () {
        overlayElement.classList.remove("active-flex");
        let result = document.querySelector("#add-friend-result");
        result.innerHTML = "Gợi ý kết bạn: coming soon";
        let inputNumberPhone = document.querySelector("#add-friend-input");
        inputNumberPhone.value = "";
    };

    btnSendRequestFriend.onclick = function () {
        if (idContactFriendRequest === -1) {
            alert("Gửi không thành công");
            return false;
        } else {
            fetchMethod(`/contacts/invitation/${idContactFriendRequest}`, {}, "POST")
                .then((res) => {
                    if (res.status === 204) {
                        alert("Kết bạn thành công");
                        btnSkip.click();
                    }
                })
                .catch((err) => {
                    console.log(err)
                })

        }
    };
}

function isVietnamesePhoneNumber(number) {
    let regex = /([84|0])+([3|5|7|8|9])+([0-9]{8})\b/;
    return regex.test(number);
}

function inputPhoneNumber() {
    let number = this.event.target.value;
    let result = document.querySelector("#add-friend-result");

    if (number) {
        if (isVietnamesePhoneNumber(number)) {
            fetchMethod(`/contacts/search/${number}`)
                .then((res) => {
                    if (res.status == 404) {
                        renderUserInAddFriend(null);
                    } else {
                        renderUserInAddFriend(res);
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
        } else {
            result.innerText = "Không phải là số điện thoại";
            idContactFriendRequest = -1;
        }
    } else {
        result.innerText = "Gợi ý kết bạn: coming soon";
        idContactFriendRequest = -1;
    }
}

function renderUserInAddFriend(user) {
    let result = document.querySelector("#add-friend-result");
    if (user) {
        result.innerHTML = `
        <img src="${user.urlAvatar}" class="add-friend__avatar" alt="avatar">
        <p class="add-friend__name">${user.firstName + " " + user.lastName}</p>
        <div class="add-friend__info-group">
            <span>Giới tính:</span>
            <span class="add-friend__gender">Nam</span>
        </div>
        <div class="add-friend__info-group">
            <span>Ngày sinh:</span>
            <span class="add-friend__dob">01/01/2001</span>
       </div> 
       `;

        if (user.isAccepted === false && user.requestSenderId === 0) {
            idContactFriendRequest = user.id;
        }
        else {
            idContactFriendRequest = -1;
        }
    } else {
        result.innerHTML = "Không tìm thấy người dùng";
        idContactFriendRequest = -1;
    }
}

function searchContacts() {
    let inputValue = this.event.target.value;
    if (inputValue) {
        let reSearch = new RegExp(inputValue, 'ig');
        allContacts = allContactsSearch.filter(function (e) {
            name = e.firstName + " " + e.lastName;
            return name.search(reSearch) != -1
        })
    } else {
        allContacts = allContactsSearch;
    }
    renderContacts(allContacts);
}