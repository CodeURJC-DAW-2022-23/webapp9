
const queryString = window.location.search;

const urlParams = new URLSearchParams(queryString);
let name = urlParams.get('name');
let type = urlParams.get('type');
let sort = urlParams.get('sort');
let order = urlParams.get('order');
let page = parseInt(urlParams.get('page'));


$(() => {

    ajax(`/results?name=${name}&type=${type}&sort=${sort}&order=${order}&page=${page}`, "#search-spinner", "#search-results");
    $('#btn-more-search').on("click", () => {
        page += 1
        ajax(`/results?name=${name}&type=${type}&sort=${sort}&order=${order}&page=${page}`, "#search-spinner", "#search-results");
    })

});

function ajax(url, spinner, where) {

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: url,
        beforeSend: function () {
            $(spinner).removeClass('d-none');
            $(spinner).addClass('d-flex');
        },
        success: function (result) {
            console.log(result);
            $(where).append(result);
        },
        complete: function () {
            $(spinner).addClass('d-none');
            $(spinner).removeClass('d-flex');
        },
    });

}

