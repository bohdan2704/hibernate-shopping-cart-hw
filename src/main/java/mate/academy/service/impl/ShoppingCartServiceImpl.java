package mate.academy.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addTicket(MovieSession movieSession, User user) {
        // Check if passed session and user are not transient?
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ShoppingCart shoppingCart = getByUser(user);
        ticket.setCart(shoppingCart);
        ticketDao.add(ticket);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        ShoppingCart shoppingCart =
                shoppingCartDao.getByUser(user).orElseThrow(NoSuchElementException::new);
        return shoppingCart;
    }

    @Override
    public void registerNewShoppingCart(User user) {
        // Maybe we should check if passed user is not transient?
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setOwner(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        List<Ticket> ticketsToBeRemoved = shoppingCart.getTickets();
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);

        for (Ticket t : ticketsToBeRemoved) {
            ticketDao.remove(t);
        }
    }
}