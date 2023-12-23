const requestUrl = '/cart/calculate';
const deleteUrl = '/cart/delete/'

const goodCount = document.querySelector('.good-count')
const sum = document.querySelector('#sum')
const plus = document.querySelector('.good-calc-up')
const minus = document.querySelector('.good-calc-down')
const deleteGood = document.querySelector('.delete-good')
const productList = document.querySelector('#product-list')

const body = {
    plus: "false",
}

plus.addEventListener('click', () => {
    console.log('Click')
    body.plus = "true"
    body.id = plus.getAttribute('data-id');
    console.log(body)
    sendRequest('POST', requestUrl, body)
        .then(data => {
            console.log(data)
            updateCount(data)
        }).catch(err => console.log(err))
})

minus.addEventListener('click', () => {
    console.log('Click')
    body.plus = "false"
    body.id = plus.getAttribute('data-id');
    console.log(body)
    sendRequest('POST', requestUrl, body)
        .then(data => {
            console.log(data)
            updateCount(data)
        }).catch(err => console.log(err))
})

deleteGood.addEventListener('click', () => {
    console.log('Click')
    const id = deleteGood.getAttribute('delete-product-id');
    console.log('id is ' + id)
    sendRequest('POST', deleteUrl + id)
        .then(data => {
            console.log(data)
            productList.innerHTML = data.products
        }).catch(err => console.log(err))
})

function updateCount(data){
    goodCount.innerHTML = data.count
    sum.innerHTML = data.sum
    console.log("count is " + data.count)
    console.log("Update sum" + data.sum)
}


function sendRequest(method, url, body = null ) {
    const headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    }
    return fetch(url, {
        method: method,
        // mode: 'no-cors',
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