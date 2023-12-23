// const requestUrl = 'https://jsonplaceholder.typicode.com/users';
const requestUrl = 'http://localhost:8083/news/3'
console.log("Hello")
// body = null
function sendRequest(method, url, body = null ) {
    const headers = {
        'Content-Type': 'application/json'
    }
    return fetch(url, {
        method: method,
        mode: 'cors',
        // body: JSON.stringify(body),
        headers: headers
    }).then(response => {
        console.log(typeof response)
        console.log(response.json().then(data => console.log(data)))
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

sendRequest('GET', requestUrl)
    .then(data => console.log(data))
    .catch(err => console.log(err))

// const body = {
//     name: 'Azat',
//     age:26
// }

// sendRequest('POST', requestUrl, body)
//     .then(data => console.log(data))
//     .catch(err => console.log(err))