alter TRIGGER TR_TRANSFER_MONEY_TO_SHOPS
    ON [dbo].[Order]
    FOR UPDATE
    AS
    BEGIN
		SET NOCOUNT ON
		declare @idins int
		declare @iddel int

		declare @status1 varchar(10)
		declare @status2 varchar(10)

		declare @dodatni bit
		declare @datum datetime

		declare @kursorIns cursor
		declare @kursorDel cursor

		set @kursorIns = cursor for
		select Id,State,DodatniPopust,ReceivedTime
		from inserted
		
		set @kursorDel = cursor for
		select Id,State
		from deleted

		open @kursorIns
		open @kursorDel

		fetch next from @kursorIns
		into @idins,@status2,@dodatni,@datum

		fetch next from @kursorDel
		into @iddel,@status1
		

		while @@FETCH_STATUS = 0
		begin

			if(@status1 = 'sent' and @status2 = 'arrived')
			begin
				declare @kursor cursor
				declare @shopId int
				declare @suma int

				set @kursor = cursor for
				select s.Id, sum(oi.Count*oi.Price)
				from OrderItems oi join Article a on oi.IdArticle=a.Id
				join Shop s on s.Id=a.IdShop
				where oi.IdOrder=@idins
				group by s.Id

				open @kursor

				fetch next from @kursor
				into @shopId, @suma

				while @@FETCH_STATUS = 0
				begin
					if(@dodatni=1)
					begin
						select @suma = @suma * 0.97
					end
					else
					begin
						select @suma = @suma * 0.95
					end

					insert into [Transaction](Amount,IdOrder,TimeOfExecution,IdShopBuyer) values(@suma,@idins,@datum,@shopId)

					fetch next from @kursor
					into @shopId, @suma
				end


			end

			fetch next from @kursorIns
			into @idins,@status2,@dodatni,@datum

			fetch next from @kursorDel
			into @iddel,@status1
		end

		close @kursorIns
		close @kursorDel

		deallocate @kursorIns
		deallocate @kursorDel
    END