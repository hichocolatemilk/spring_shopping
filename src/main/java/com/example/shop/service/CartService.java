package com.example.shop.service;

import com.example.shop.dto.CartDetailDto;
import com.example.shop.dto.CartItemDto;
import com.example.shop.dto.CartOrderDto;
import com.example.shop.dto.OrderDto;
import com.example.shop.entity.Cart;
import com.example.shop.entity.CartItem;
import com.example.shop.entity.Item;
import com.example.shop.entity.Member;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    public Long addCart(CartItemDto cartItemDto, String email){

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        }else{
            CartItem cartItem = CartItem.createItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email){
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null){
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        Member curMember = memberRepository.findByEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();

        if(!StringUtils.equals(curMember.getEmail(),
                savedMember.getEmail())){
            return false;
        }
        return true;
    }

    public void updateCartItemCount(Long cartItemId, int count){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email){
        List<OrderDto> orderDtoList = new ArrayList<>();

        for(CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.Orders(orderDtoList, email);

        for(CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }
}
