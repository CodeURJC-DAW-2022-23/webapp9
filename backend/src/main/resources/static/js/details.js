const fileName = location.href.split("/");

const type = fileName[4];
const id = parseInt(fileName[5]);

pageInfo = 0;
pageReview = 0;

$(() => {

    ajax(`/${type}/${id}/information?page=${pageInfo}`, "#information-spinner", "#info-item-bg");
    $('#btn-more-information').on("click", () => {
        pageInfo += 1;
        ajax(`/${type}/${id}/information?page=${pageInfo}`, "#information-spinner", "#info-item-bg");
    });

    if (type === "itinerary") {
        ajax(`/${type}/${id}/reviews?page=${pageInfo}`, "#reviews-spinner", "#info-review-bg");
        $('#btn-more-reviews').on("click", () => {
            pageInfo += 1;
            ajax(`/${type}/${id}/reviews?page=${pageInfo}`, "#reviews-spinner", "#info-review-bg");
        });
    }

});

function ajax(url, spinner, where) {

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: url,
        beforeSend: function() {
            $(spinner).removeClass('d-none');
            $(spinner).addClass('d-flex');
        },
        success: function(result) {
            console.log(result)
            $(where).append(result);
        },
        complete: function() {
            $(spinner).addClass('d-none');
            $(spinner).removeClass('d-flex');
        },
    });

}