
const requestUrl = '/cart/buy/'
const addButton = document.querySelector('.add-to-cart');

const body = null

function addInCart(i){
    const isAuth = JSON.parse(addButton.getAttribute("is-auth"));
    const index = i.getAttribute("good-id");

    if (isAuth) {
        sendRequest('POST', requestUrl+index, body)
            .then(data => {
                console.log(data)
            })
            .catch(err => console.log(err))
    }
}

function sendRequest(method, url, body = null ) {
    const headers = {
        'Content-Type': 'application/json'
    }
    console.log("body is " + JSON.stringify(body))
    return fetch(url, {
        method: method,
        mode: 'no-cors',
        body: JSON.stringify(body),
        headers: headers
    }).then(response => {
        if (response.ok) {
            return response.json();
        }
        return response.json().then(error => {
            const e = new Error("Что-то пошло не так!");
            e.data = error
            throw e
        })
    })
}