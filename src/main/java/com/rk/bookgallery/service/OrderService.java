package com.rk.bookgallery.service;

import com.rk.bookgallery.dto.OrderItemResponse;
import com.rk.bookgallery.dto.OrderRequest;
import com.rk.bookgallery.dto.OrderResponse;
import com.rk.bookgallery.entity.Book;
import com.rk.bookgallery.entity.CartItem;
import com.rk.bookgallery.entity.Order;
import com.rk.bookgallery.entity.OrderItem;
import com.rk.bookgallery.entity.OrderStatus;
import com.rk.bookgallery.entity.User;
import com.rk.bookgallery.exception.BadRequestException;
import com.rk.bookgallery.exception.ResourceNotFoundException;
import com.rk.bookgallery.repository.BookRepository;
import com.rk.bookgallery.repository.CartItemRepository;
import com.rk.bookgallery.repository.OrderRepository;
import com.rk.bookgallery.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        CartItemRepository cartItemRepository,
                        BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public OrderResponse createOrder(String email, OrderRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Empty cart");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setStatus(OrderStatus.PENDING);

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cartItems) {
            Book book = item.getBook();
            if (book.getStock() < item.getQuantity()) {
                throw new BadRequestException("Out of stock: " + book.getTitle());
            }

            book.setStock(book.getStock() - item.getQuantity());
            bookRepository.save(book);

            OrderItem orderItem = new OrderItem(order, book, item.getQuantity(), book.getPrice());
            total += book.getPrice() * item.getQuantity();
            orderItems.add(orderItem);
        }

        order.setTotalAmount(total + 49.0);
        order.setOrderItems(orderItems);
        orderItems.forEach(item -> item.setOrder(order));

        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteByUser(user);

        return mapToResponse(savedOrder);
    }

    public List<OrderResponse> getOrdersForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return orderRepository.findByUserOrderByOrderDateDesc(user).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse getOrderById(String email, Long id) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Unauthorized access to order");
        }
        return mapToResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        return mapToResponse(orderRepository.save(order));
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderDate(order.getOrderDate());
        response.setTotalAmount(order.getTotalAmount());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setStatus(order.getStatus());
        response.setDeliveryAddress(order.getDeliveryAddress());

        List<OrderItemResponse> items = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setBookId(item.getBook().getId());
            itemResponse.setTitle(item.getBook().getTitle());
            itemResponse.setAuthor(item.getBook().getAuthor());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPrice(item.getPrice());
            items.add(itemResponse);
        }
        response.setItems(items);
        return response;
    }
}
