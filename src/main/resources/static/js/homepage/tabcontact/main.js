function switchAction(actionClickTab) {
    let actionActiveTab = document.querySelector(".action-item.action-item-active");
    if (actionActiveTab !== actionClickTab)
    {
        actionActiveTab.classList.remove("action-item-active");
        actionClickTab.classList.add("action-item-active");
        return true;
    }
    else {
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
    }

}

function openGroupList() {
    let actionClickTab = this.event.target;
    let friendRequestListElement = document.querySelector("#friend-request-list");
    let groupListElement = document.querySelector("#group-list");

    if (switchAction(actionClickTab)) {
        groupListElement.classList.add("active-block");
        friendRequestListElement.classList.remove("active-block");
    }
}

function membersInGroup() {
    let overlayElement = document.querySelector(`[action-overlay-name="members-in-group"]`);
    overlayElement.classList.add("active-flex")
    let btnCancel = overlayElement.querySelector(".btn-cancel")
    console.log(overlayElement)
    btnCancel.onclick = function () {
        overlayElement.classList.remove("active-flex")
    }

}

function openCreateGroup() {
    let overlayElement = document.querySelector("#overlay-create-group");
    overlayElement.classList.add("active-flex")
    let btnSkip = overlayElement.querySelector(".btn.btn-secondary")
    let btnDone = overlayElement.querySelector(".btn.btn-primary")

    btnSkip.onclick = function () {
        overlayElement.classList.remove("active-flex")
    }
}

function openAddFriend() {
    let overlayElement = document.querySelector("#overlay-add-friend");
    overlayElement.classList.add("active-flex")
    let btnSkip = overlayElement.querySelector(".btn.btn-secondary")
    let btnDone = overlayElement.querySelector(".btn.btn-primary")

    btnSkip.onclick = function () {
        overlayElement.classList.remove("active-flex")
    }
}



function isVietnamesePhoneNumber(number) {
    let regex =  /([84|0])+([3|5|7|8|9])+([0-9]{8})\b/ ;
    return regex.test(number)
}

function inputPhoneNumber() {
    let number = this.event.target.value
    let result = document.querySelector(".add-friend__result");

    if (number)
    {
        if (isVietnamesePhoneNumber(number)) {
            result.innerHTML = `
                <p>test</p>
             `
        }
        else {
            console.log("khong phai so vn")
            result.innerText = "Không phải là số điện thoại"
        }

    }
    else {
        result.innerText = "Gợi ý kết bạn: coming soon";
    }


}