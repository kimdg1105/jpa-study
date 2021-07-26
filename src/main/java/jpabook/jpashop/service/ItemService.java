package jpabook.jpashop.service;


import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, Book bookParam){
        Book findItem = (Book) itemRepository.findOne(itemId); // 아이디를 기반으로 영속성 컨텍스트 내의 엔티티를 가져온다.
        findItem.setStockQuantity(bookParam.getStockQuantity());
        findItem.setName(bookParam.getName());
        findItem.setPrice(bookParam.getPrice());
        findItem.setAuthor(bookParam.getAuthor());
        findItem.setIsbn(bookParam.getIsbn());
        itemRepository.save(findItem);


    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }


}
