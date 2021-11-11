const getUrl = window.location
const BASE_URL = getUrl.protocol + "//" + getUrl.host
const VALID_STATUS = [200, 201, 202, 203, 204];
async function fetchMethod(route, data = {}, method = "get") {
    const ALLOWED_METHODS = ["get", "post", "put", "patch", "delete"];

    if (ALLOWED_METHODS.includes(method.toLowerCase())) {
        let requestData = {
            method: method,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }

        if (method === "get")
            delete requestData.body;

        return (await fetch((BASE_URL + route), requestData)).json();
    }
}