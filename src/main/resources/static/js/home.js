var allContacts

function start() {
    fetchContacts()
}

start()

function fetchContacts() {
    fetchMethod("/contacts")
        .then(data => {
            allContacts = data;
            rederContacts(allContacts)
        })
        .catch(err => {
            console.log(err)
        })
}

function rederContacts(contacts) {
    let listContacts = document.querySelector("#list-contacts");
    contacts.map(contact => {
        let contactDiv = document.createElement("div")
        contactDiv.setAttribute("class", "contact")
        contactDiv.innerHTML = `
            <div class="contact__avatar">
                <img src="https://source.unsplash.com/random" alt="" />
                <div class="online"></div>
            </div>
            <div class="contact__description">
                <div class="contact__name">
                <p>${contact.firstName} ${contact.surname}</p>
            </div>
            <div class="contact__message">
                <p>Hello you, today i feel so good</p>
                <span>1 giờ</span>
                <i class="fas fa-check-circle"></i>
            </div>
        `
        listContacts.append(contactDiv)
    })
}
