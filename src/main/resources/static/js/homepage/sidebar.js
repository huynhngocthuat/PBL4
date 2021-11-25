function switchTab () {
    let tabCLicked =  this.event.target.getAttribute("tab-name")
        ?  this.event.target :  this.event.target.parentNode
    let tabActive = document.querySelector(".tab.active")
    let tabClickedName = tabCLicked.getAttribute("tab-name")

    let tabElementActive = document.querySelector(".main-container.active-flex")
    let tabElementWithTabClickedName = document.querySelector(`#${tabClickedName}`)

    if (tabCLicked !== tabActive) {
        tabActive.classList.remove("active");
        tabCLicked.classList.add("active");
        tabElementActive.classList.remove("active-flex");
        tabElementWithTabClickedName.classList.add("active-flex");
    }
}