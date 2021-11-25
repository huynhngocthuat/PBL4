function isVietnamesePhoneNumber(number) {
    let regex =  /([84|0])+([3|5|7|8|9])+([0-9]{8})\b/ ;
    return regex.test(number)
}

function checkPhoneNumber() {
    let number = this.event.target.value
    let msg = document.querySelector(".form-group-error");
    if (number)
    {
        if (!isVietnamesePhoneNumber(number)) {
            msg.innerText = "Không phải là số điện thoại";
        }
        else {
            msg.innerText = "";
        }
    }
    else {
        msg.innerText = "";
    }
}

function checkAfterSubmit() {
    let phoneNumberInput = document.getElementById("exampleInputPhone");
    let passwordInput = document.getElementById("exampleInputPassword");
    let loginFormError = document.querySelector(".login-form-error");
    if (isVietnamesePhoneNumber(phoneNumberInput.value) && passwordInput.value.length >= 6)
    {
        loginFormError.innerText = "";
        document.getElementById("submit-form").click();
    }
    else {
        loginFormError.innerText = "Password must be at least 6 characters";
    }
}
