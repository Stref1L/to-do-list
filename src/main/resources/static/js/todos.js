$("#sortable").sortable();
$("#sortable").disableSelection();

countTodos();

// all done btn
$("#checkAll").click(function () {
    AllDone();
});

//create todo
$('.add-todo').on('keypress', function (e) {
    e.preventDefault
    if (e.which == 13) {
        if ($(this).val() != '') {
            var todo = $(this).val();
            createTodo(todo);
            countTodos();
        }
    }
});

// mark task as done
$('.todolist').on('change', '#sortable li input[type="checkbox"]', function () {
    var checkBox = $(this);
    if (checkBox.prop('checked')) {
        var todoItemId = checkBox.closest('li').data('id');
        var todoItemName = checkBox.parent().parent().find('p').text();

        $.ajax({
            url: '/todos/' + todoItemId,
            type: 'PUT',
            async: false,
            success: function (result) {
                checkBox.closest('li').addClass('remove');
                done(todoItemId, todoItemName);
                countTodos();
            },
            error: function (error) {
                checkBox.prop('checked', false);
                alert('Could not mark the task as done!');
            }
        });
    }
});


//delete done task from "already done"
$('.todolist').on('click', '.remove-item', function () {
    removeItem(this);
});

// count tasks
function countTodos() {
    var count = $("#sortable li").length;
    $('.count-todos').html(count);
}

//create task
function createTodo(text) {
    $.ajax({
        url: '/todos',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ name: text }),
        success: function (result) {
            var todoId = result.id; // Предполагается, что сервер возвращает объект с ID задачи
            var markup = '<li class="ui-state-default" data-id="' + todoId + '"><div class="checkbox"><label><input type="checkbox" value="" /><p>' + text + '</p></label></div></li>';

            $('#sortable').append(markup);
            $('.add-todo').val('');
            countTodos();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert('Could not add the task!');
        }
    });
}


//mark task as done
function done(doneItemId, doneItemName) {
    var markup = '<li data-id="' + doneItemId + '"><p>' + doneItemName + '</p><button class="btn btn-default btn-xs pull-right remove-item"><span class="glyphicon glyphicon-remove"></span></button></li>';
    $('#done-items').append(markup);
    $('.remove').remove();
}

//mark all tasks as done
function AllDone() {
    var myArray = [];

    $('#sortable li').each(function () {
        var id = $(this).data('id');
        var text = $(this).find('p').text();
        myArray.push({id: id, text: text});
    });

    var notAddedTasks = [];

    // add to done
    for (i = 0; i < myArray.length; i++) {
        var todo = myArray[i];
        $.ajax({
            url: '/todos/' + todo.id,
            type: 'PUT',
            async: false,
            success: function(result) {
                $('#done-items').append('<li data-id="' + todo.id + '"><p>' + todo.text + '</p><button class="btn btn-default btn-xs pull-right remove-item"><span class="glyphicon glyphicon-remove"></span></button></li>');
            },
            error: function (error) {
                notAddedTasks.push(todo);
            }
        });
    }

    $('#sortable li').remove();

    for (i = 0; i < notAddedTasks.length; i++) {
        var todo = notAddedTasks[i];
        var markup = '<li class="ui-state-default" data-id="' + todo.id + '"><div class="checkbox"><label><input type="checkbox" value="" /><p>' + todo.text + '</p></label></div></li>';
        $('#sortable').append(markup);
    }
    countTodos();
}


//remove done task from list
function removeItem(element) {
    var todoItemId = $(element).closest('li').data('id');

    $.ajax({
        url: '/todos/' + todoItemId,
        type: 'DELETE',
        async: false,
        success: function (result) {
            $(element).closest('li').remove();
        },
        error: function (error) {
            alert('Could not delete the task!');
        }
    });
}
