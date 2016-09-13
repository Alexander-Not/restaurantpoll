$('#confirmRemove').on(
		'show.bs.modal',
		function(event) {

			var button = $(event.relatedTarget);

			var codeRestaurant = button.data('id');
			var nameRestaurant = button.data('name');

			var modal = $(this);
			var form = modal.find('form');
			var action = form.data('url-base');

			if (!action.endsWith('/')) {
				action += '/';
			}

			form.attr('action', action + codeRestaurant);

			modal.find('.modal-body span').html(
					'Tem certeza que deseja excluir o restaurante <strong>'
							+ nameRestaurant + '</strong>?')

		});