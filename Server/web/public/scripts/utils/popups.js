function showSuccess(title, message) {
    if (title === undefined) {
        title = "Success!";
    }
    Swal.fire(title, message, 'success'
    )
}

function showError(title, message) {
    if (title === undefined) {
        title = "Error!";
    }
    Swal.fire(title, message, "error");
}

function areYouSureMessage(message, afterMessage, title) {
    if (title === undefined) {
        title = "Are you sure?";
    }
    Swal.fire({
        title: title,
        text: message,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire(
                'Success!',
                'afterMessage',
                'success'
            )
        }
    })
}