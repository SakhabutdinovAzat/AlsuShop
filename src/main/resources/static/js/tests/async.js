// const requestUrl = 'https://jsonplaceholder.typicode.com/users';
const requestUrl = 'http://localhost:8083/news/3';

function sendRequest(method, url, body = null) {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();

        xhr.open(method, url);

        xhr.responseType = 'json'
        // xhr.setRequestHeader('Content-Type', 'application/json')

        xhr.onload = () => {
            if (xhr.status >= 400) {
                reject(xhr.response)
            } else {
                resolve(xhr.response)
            }
        }

        xhr.onerror = () => {
            reject(xhr.response)
        }

        xhr.send()
        // xhr.send(JSON.stringify(body))
    })
}

sendRequest('GET', requestUrl)
    .then(data => console.log(data))
    .catch(err => console.log(err))

// const body = {
//     name: 'Azat',
//     age:26
// }
//
// sendRequest('POST', requestUrl, body)
//     .then(data => console.log(data))
//     .catch(err => console.log(err))